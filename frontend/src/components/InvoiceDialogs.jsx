import React, { useEffect, useMemo, useState } from "react";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Stack,
  TextField,
  Typography
} from "@mui/material";

const initialForm = {
  nameCustomer: "",
  custNumber: "",
  invoiceId: "",
  totalOpenAmount: "",
  dueInDate: "",
  notes: ""
};

export function AddDialog({ open, onClose, onSubmit, busy }) {
  const [form, setForm] = useState(initialForm);

  useEffect(() => {
    if (!open) {
      setForm(initialForm);
    }
  }, [open]);

  const handleSubmit = () => {
    onSubmit({
      ...form,
      totalOpenAmount: Number(form.totalOpenAmount)
    });
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth>
      <DialogTitle>Add Invoice</DialogTitle>
      <DialogContent>
        <Stack spacing={2} sx={{ mt: 1 }}>
          <TextField label="Customer Name" value={form.nameCustomer} onChange={(e) => setForm((prev) => ({ ...prev, nameCustomer: e.target.value }))} />
          <TextField label="Customer Number" value={form.custNumber} onChange={(e) => setForm((prev) => ({ ...prev, custNumber: e.target.value }))} />
          <TextField label="Invoice ID" value={form.invoiceId} onChange={(e) => setForm((prev) => ({ ...prev, invoiceId: e.target.value }))} />
          <TextField label="Invoice Amount" type="number" value={form.totalOpenAmount} onChange={(e) => setForm((prev) => ({ ...prev, totalOpenAmount: e.target.value }))} />
          <TextField label="Due Date" type="date" InputLabelProps={{ shrink: true }} value={form.dueInDate} onChange={(e) => setForm((prev) => ({ ...prev, dueInDate: e.target.value }))} />
          <TextField label="Notes" multiline minRows={2} value={form.notes} onChange={(e) => setForm((prev) => ({ ...prev, notes: e.target.value }))} />
        </Stack>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>
        <Button onClick={handleSubmit} variant="contained" disabled={busy}>Add</Button>
      </DialogActions>
    </Dialog>
  );
}

export function EditDialog({ open, onClose, onSubmit, busy, selectedInvoice }) {
  const [totalOpenAmount, setTotalOpenAmount] = useState("");
  const [dueInDate, setDueInDate] = useState("");
  const [notes, setNotes] = useState("");

  useEffect(() => {
    if (!selectedInvoice) {
      return;
    }
    setTotalOpenAmount(selectedInvoice.totalOpenAmount ?? "");
    setDueInDate(selectedInvoice.dueInDate ?? "");
    setNotes(selectedInvoice.notes ?? "");
  }, [selectedInvoice]);

  const payload = useMemo(
    () => ({
      totalOpenAmount: totalOpenAmount === "" ? null : Number(totalOpenAmount),
      dueInDate: dueInDate || null,
      notes
    }),
    [totalOpenAmount, dueInDate, notes]
  );

  return (
    <Dialog open={open} onClose={onClose} fullWidth>
      <DialogTitle>Edit Invoice</DialogTitle>
      <DialogContent>
        {selectedInvoice ? (
          <Stack spacing={2} sx={{ mt: 1 }}>
            <Typography variant="body2">Invoice ID: {selectedInvoice.invoiceId}</Typography>
            <TextField
              label="Invoice Amount"
              type="number"
              value={totalOpenAmount}
              onChange={(e) => setTotalOpenAmount(e.target.value)}
            />
            <TextField
              label="Due Date"
              type="date"
              InputLabelProps={{ shrink: true }}
              value={dueInDate}
              onChange={(e) => setDueInDate(e.target.value)}
            />
            <TextField label="Notes" multiline minRows={2} value={notes} onChange={(e) => setNotes(e.target.value)} />
          </Stack>
        ) : null}
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>
        <Button onClick={() => onSubmit(payload)} variant="contained" disabled={busy || !selectedInvoice}>
          Save
        </Button>
      </DialogActions>
    </Dialog>
  );
}

export function DeleteDialog({ open, onClose, onConfirm, selectedCount, busy }) {
  return (
    <Dialog open={open} onClose={onClose} fullWidth>
      <DialogTitle>Delete Invoice(s)?</DialogTitle>
      <DialogContent>
        <Typography>
          You are about to permanently delete {selectedCount} record(s). This action cannot be undone.
        </Typography>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>
        <Button color="error" variant="contained" onClick={onConfirm} disabled={busy || selectedCount < 1}>
          Delete
        </Button>
      </DialogActions>
    </Dialog>
  );
}
