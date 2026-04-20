import React from "react";
import { Box, Typography } from "@mui/material";
import { ReactComponent as CompanyLogo } from "../assets/company.svg";
import { ReactComponent as ProductLogo } from "../assets/logo.svg";

export default function Header() {
  return (
    <Box
      sx={{
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        gap: 2,
        mb: 2
      }}
    >
      <CompanyLogo style={{ height: 34 }} />
      <Typography variant="h5" sx={{ color: "#fff", fontWeight: 700 }}>
        Invoice List
      </Typography>
      <ProductLogo style={{ height: 34 }} />
    </Box>
  );
}
