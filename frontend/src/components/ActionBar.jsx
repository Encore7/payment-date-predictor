import React from "react";
import {
  Box,
  Button,
  Stack,
  TextField,
  Typography
} from "@mui/material";

export default function ActionBar({
  selectedCount,
  search,
  onSearchChange,
  onSearchSubmit,
  onAdd,
  onEdit,
  onDelete,
  onPredict,
  busy
}) {
  return (
    <Stack
      direction={{ xs: "column", md: "row" }}
      alignItems={{ xs: "stretch", md: "center" }}
      justifyContent="space-between"
      spacing={2}
      sx={{ mb: 2 }}
    >
      <Stack direction="row" spacing={1} flexWrap="wrap">
        <Button variant="outlined" onClick={onAdd} disabled={busy}>
          Add
        </Button>
        <Button variant="outlined" onClick={onEdit} disabled={busy || selectedCount !== 1}>
          Edit
        </Button>
        <Button variant="outlined" color="error" onClick={onDelete} disabled={busy || selectedCount < 1}>
          Delete
        </Button>
        <Button variant="contained" onClick={onPredict} disabled={busy || selectedCount < 1}>
          Predict
        </Button>
      </Stack>
      <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
        <TextField
          size="small"
          value={search}
          onChange={(e) => onSearchChange(e.target.value)}
          placeholder="Search customer/invoice"
        />
        <Button variant="outlined" onClick={onSearchSubmit} disabled={busy}>
          Search
        </Button>
      </Box>
      <Typography variant="body2" sx={{ color: "#c8d2da" }}>
        {selectedCount} selected
      </Typography>
    </Stack>
  );
}
