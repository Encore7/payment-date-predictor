import React, { useEffect, useMemo, useState } from "react";
import { Alert, Box, Container, CssBaseline, Snackbar, ThemeProvider, createTheme } from "@mui/material";
import { addInvoice, deleteInvoices, predictInvoices, updateInvoice } from "./api/invoices";
import ActionBar from "./components/ActionBar";
import Header from "./components/Header";
import InvoiceTable from "./components/InvoiceTable";
import { AddDialog, DeleteDialog, EditDialog } from "./components/InvoiceDialogs";
import { useInvoices } from "./hooks/useInvoices";

const theme = createTheme({
  palette: {
    mode: "dark",
    primary: { main: "#14aff1" },
    background: { default: "#2f4250", paper: "#273d49" }
  }
});

export default function App() {
  const { rows, hasNext, loading, search, setRows, resetAndLoad, loadMore } = useInvoices();
  const [selectedIds, setSelectedIds] = useState([]);
  const [dialog, setDialog] = useState(null);
  const [toast, setToast] = useState(null);
  const [searchInput, setSearchInput] = useState(search);
  const [busy, setBusy] = useState(false);

  useEffect(() => {
    resetAndLoad();
  }, [resetAndLoad]);

  const selectedInvoice = useMemo(
    () => rows.find((row) => row.invoiceId === selectedIds[0]),
    [rows, selectedIds]
  );

  const toggleSelect = (invoiceId) => {
    setSelectedIds((prev) =>
      prev.includes(invoiceId) ? prev.filter((id) => id !== invoiceId) : [...prev, invoiceId]
    );
  };

  const withBusy = async (action, successMessage) => {
    setBusy(true);
    try {
      await action();
      if (successMessage) {
        setToast({ severity: "success", message: successMessage });
      }
    } catch (error) {
      const message = error?.response?.data?.error || "Request failed";
      setToast({ severity: "error", message });
    } finally {
      setBusy(false);
    }
  };

  const refresh = async () => {
    await resetAndLoad(searchInput);
    setSelectedIds([]);
  };

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Container maxWidth="xl" sx={{ py: 3 }}>
        <Header />
        <Box sx={{ backgroundColor: "#273d49", p: 2, borderRadius: 1 }}>
          <ActionBar
            selectedCount={selectedIds.length}
            search={searchInput}
            onSearchChange={setSearchInput}
            onSearchSubmit={() => withBusy(() => resetAndLoad(searchInput), null)}
            onAdd={() => setDialog("add")}
            onEdit={() => setDialog("edit")}
            onDelete={() => setDialog("delete")}
            onPredict={() =>
              withBusy(async () => {
                await predictInvoices(selectedIds);
                await refresh();
              }, "Prediction applied")
            }
            busy={busy || loading}
          />

          <InvoiceTable
            rows={rows}
            selectedIds={selectedIds}
            onToggle={toggleSelect}
            onLoadMore={() => withBusy(loadMore, null)}
            hasNext={hasNext}
          />
        </Box>
      </Container>

      <AddDialog
        open={dialog === "add"}
        onClose={() => setDialog(null)}
        busy={busy}
        onSubmit={(payload) =>
          withBusy(async () => {
            await addInvoice(payload);
            setDialog(null);
            await refresh();
          }, "Invoice added")
        }
      />

      <EditDialog
        open={dialog === "edit"}
        onClose={() => setDialog(null)}
        busy={busy}
        selectedInvoice={selectedInvoice}
        onSubmit={(payload) =>
          withBusy(async () => {
            await updateInvoice(selectedIds[0], payload);
            setDialog(null);
            await refresh();
          }, "Invoice updated")
        }
      />

      <DeleteDialog
        open={dialog === "delete"}
        onClose={() => setDialog(null)}
        selectedCount={selectedIds.length}
        busy={busy}
        onConfirm={() =>
          withBusy(async () => {
            await deleteInvoices(selectedIds);
            setDialog(null);
            await refresh();
          }, "Invoice(s) deleted")
        }
      />

      <Snackbar
        open={Boolean(toast)}
        autoHideDuration={3000}
        onClose={() => setToast(null)}
        anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
      >
        {toast ? (
          <Alert onClose={() => setToast(null)} severity={toast.severity} variant="filled">
            {toast.message}
          </Alert>
        ) : null}
      </Snackbar>
    </ThemeProvider>
  );
}
