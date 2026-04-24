import React from "react";
import InfiniteScroll from "react-infinite-scroll-component";
import {
  Box,
  Checkbox,
  CircularProgress,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow
} from "@mui/material";

export default function InvoiceTable({ rows, selectedIds, onToggle, onLoadMore, hasNext }) {
  return (
    <Paper sx={{ backgroundColor: "#273d49cc" }}>
      <TableContainer id="scrollableTable" sx={{ maxHeight: 620 }}>
        <InfiniteScroll
          dataLength={rows.length}
          next={onLoadMore}
          hasMore={hasNext}
          scrollableTarget="scrollableTable"
          loader={
            <Box sx={{ display: "flex", justifyContent: "center", py: 2 }}>
              <CircularProgress size={22} />
            </Box>
          }
        >
          <Table stickyHeader>
            <TableHead>
              <TableRow>
                <TableCell padding="checkbox">Select</TableCell>
                <TableCell>Customer Name</TableCell>
                <TableCell>Customer No.</TableCell>
                <TableCell>Invoice Id</TableCell>
                <TableCell>Invoice Amount</TableCell>
                <TableCell>Due Date</TableCell>
                <TableCell>Predicted Payment Date</TableCell>
                <TableCell>Predicted Ageing Bucket</TableCell>
                <TableCell>Notes</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {rows.map((row) => {
                const checked = selectedIds.includes(row.invoiceId);
                return (
                  <TableRow key={row.invoiceId} hover>
                    <TableCell padding="checkbox">
                      <Checkbox
                        checked={checked}
                        onChange={() => onToggle(row.invoiceId)}
                      />
                    </TableCell>
                    <TableCell>{row.nameCustomer}</TableCell>
                    <TableCell>{row.custNumber}</TableCell>
                    <TableCell>{row.invoiceId}</TableCell>
                    <TableCell>{row.totalOpenAmount}</TableCell>
                    <TableCell>{row.dueInDate || "-"}</TableCell>
                    <TableCell>{row.predictedPaymentDate || "--"}</TableCell>
                    <TableCell>{row.predictedAgeingBucket || "--"}</TableCell>
                    <TableCell>{row.notes || ""}</TableCell>
                  </TableRow>
                );
              })}
            </TableBody>
          </Table>
        </InfiniteScroll>
      </TableContainer>
    </Paper>
  );
}
