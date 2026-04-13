import { api } from "./client";

export const fetchInvoices = (page, size, search) =>
  api.get("/invoices", { params: { page, size, search: search || undefined } });

export const addInvoice = (payload) => api.post("/invoices", payload);

export const updateInvoice = (invoiceId, payload) => api.put(`/invoices/${invoiceId}`, payload);

export const deleteInvoices = (invoiceIds) =>
  api.delete("/invoices", { data: { invoiceIds } });

export const predictInvoices = (invoiceIds) =>
  api.post("/invoices/predict", { invoiceIds });
