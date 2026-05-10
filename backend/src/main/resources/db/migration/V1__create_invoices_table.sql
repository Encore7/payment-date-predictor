CREATE TABLE invoices (
  invoice_id VARCHAR(32) PRIMARY KEY,
  business_code VARCHAR(20),
  cust_number VARCHAR(32),
  name_customer VARCHAR(255),
  clear_date DATE NULL,
  business_year INT,
  doc_id VARCHAR(32),
  posting_date DATE NULL,
  document_create_date DATE NULL,
  due_in_date DATE NULL,
  invoice_currency VARCHAR(10),
  document_type VARCHAR(10),
  posting_id INT NULL,
  area_business VARCHAR(100) NULL,
  total_open_amount DECIMAL(15,2) NULL,
  baseline_create_date DATE NULL,
  customer_payment_terms VARCHAR(30) NULL,
  is_open TINYINT,
  notes TEXT NULL,
  predicted_payment_date DATE NULL,
  predicted_ageing_bucket VARCHAR(50) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_invoices_cust_number ON invoices (cust_number);
CREATE INDEX idx_invoices_name_customer ON invoices (name_customer);
CREATE INDEX idx_invoices_due_in_date ON invoices (due_in_date);
