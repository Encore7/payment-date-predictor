package com.paymentpredictor.service;

import com.paymentpredictor.repository.InvoiceRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CsvSeedService implements CommandLineRunner {

  private final InvoiceRepository invoiceRepository;
  private final JdbcTemplate jdbcTemplate;

  @Value("${app.seed.csv-path}")
  private Resource csvResource;

  @Override
  public void run(String... args) throws Exception {
    if (invoiceRepository.count() > 0) {
      log.info("Skipping CSV seed because invoices table already has data");
      return;
    }

    List<String[]> rows = new ArrayList<>(5000);
    int total = 0;

    try (BufferedReader reader =
        new BufferedReader(
            new InputStreamReader(csvResource.getInputStream(), StandardCharsets.UTF_8))) {

      reader.readLine();
      String line;
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",", -1);
        if (data.length < 19) {
          continue;
        }
        rows.add(data);
        if (rows.size() == 5000) {
          insertBatch(rows);
          total += rows.size();
          rows.clear();
          log.info("Seeded {} rows", total);
        }
      }
    }

    if (!rows.isEmpty()) {
      insertBatch(rows);
      total += rows.size();
    }

    log.info("CSV seed completed with {} rows", total);
  }

  private void insertBatch(List<String[]> rows) {
    String sql =
        """
        INSERT IGNORE INTO invoices (
          invoice_id, business_code, cust_number, name_customer, clear_date, business_year,
          doc_id, posting_date, document_create_date, due_in_date, invoice_currency,
          document_type, posting_id, area_business, total_open_amount, baseline_create_date,
          customer_payment_terms, is_open, notes, predicted_payment_date, predicted_ageing_bucket
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

    jdbcTemplate.batchUpdate(
        sql,
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws java.sql.SQLException {
            String[] d = rows.get(i);
            String invoiceId = numberToString(d[17]);
            if (invoiceId == null || invoiceId.isBlank()) {
              invoiceId = numberToString(d[5]);
            }

            ps.setString(1, invoiceId);
            ps.setString(2, emptyToNull(d[0]));
            ps.setString(3, emptyToNull(d[1]));
            ps.setString(4, emptyToNull(d[2]));
            ps.setDate(5, toSqlDate(parseTimestampDate(d[3])));
            ps.setObject(6, parseInteger(d[4]));
            ps.setString(7, numberToString(d[5]));
            ps.setDate(8, toSqlDate(parseDate(d[6])));
            ps.setDate(9, toSqlDate(parseCompactDate(d[7])));
            ps.setDate(10, toSqlDate(parseMaybeDecimalCompactDate(d[9])));
            ps.setString(11, emptyToNull(d[10]));
            ps.setString(12, emptyToNull(d[11]));
            ps.setObject(13, parseInteger(d[12]));
            ps.setString(14, emptyToNull(d[13]));
            ps.setBigDecimal(15, parseAmount(d[14]));
            ps.setDate(16, toSqlDate(parseMaybeDecimalCompactDate(d[15])));
            ps.setString(17, emptyToNull(d[16]));
            Integer isOpen = parseInteger(d[18]);
            ps.setObject(18, isOpen == null ? 0 : isOpen);
            ps.setString(19, null);
            ps.setDate(20, null);
            ps.setString(21, null);
          }

          @Override
          public int getBatchSize() {
            return rows.size();
          }
        });
  }

  private String emptyToNull(String value) {
    if (value == null) {
      return null;
    }
    String trimmed = value.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }

  private String numberToString(String value) {
    String v = emptyToNull(value);
    if (v == null) {
      return null;
    }
    try {
      return new BigDecimal(v).setScale(0, RoundingMode.HALF_UP).toPlainString();
    } catch (NumberFormatException ex) {
      return v;
    }
  }

  private Integer parseInteger(String value) {
    String v = emptyToNull(value);
    if (v == null) {
      return null;
    }
    try {
      return new BigDecimal(v).setScale(0, RoundingMode.HALF_UP).intValue();
    } catch (NumberFormatException ex) {
      return null;
    }
  }

  private BigDecimal parseAmount(String value) {
    String v = emptyToNull(value);
    if (v == null) {
      return null;
    }
    try {
      return new BigDecimal(v).setScale(2, RoundingMode.HALF_UP);
    } catch (NumberFormatException ex) {
      return null;
    }
  }

  private LocalDate parseDate(String value) {
    String v = emptyToNull(value);
    if (v == null) {
      return null;
    }
    try {
      return LocalDate.parse(v);
    } catch (Exception ex) {
      return null;
    }
  }

  private LocalDate parseTimestampDate(String value) {
    String v = emptyToNull(value);
    if (v == null) {
      return null;
    }
    try {
      return LocalDateTime.parse(v, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate();
    } catch (Exception ex) {
      return null;
    }
  }

  private LocalDate parseCompactDate(String value) {
    String v = emptyToNull(value);
    if (v == null) {
      return null;
    }
    try {
      return LocalDate.parse(v, DateTimeFormatter.ofPattern("yyyyMMdd"));
    } catch (Exception ex) {
      return null;
    }
  }

  private LocalDate parseMaybeDecimalCompactDate(String value) {
    String v = emptyToNull(value);
    if (v == null) {
      return null;
    }
    if (v.endsWith(".0")) {
      v = v.substring(0, v.length() - 2);
    }
    return parseCompactDate(v);
  }

  private Date toSqlDate(LocalDate localDate) {
    return localDate == null ? null : Date.valueOf(localDate);
  }
}
