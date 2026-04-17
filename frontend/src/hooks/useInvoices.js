import { useCallback, useState } from "react";
import { fetchInvoices } from "../api/invoices";
import { PAGE_SIZE } from "../constants/config";

export function useInvoices() {
  const [rows, setRows] = useState([]);
  const [page, setPage] = useState(1);
  const [hasNext, setHasNext] = useState(true);
  const [loading, setLoading] = useState(false);
  const [search, setSearch] = useState("");

  const resetAndLoad = useCallback(
    async (searchValue = "") => {
      setLoading(true);
      try {
        const response = await fetchInvoices(1, PAGE_SIZE, searchValue);
        setRows(response.data.data);
        setHasNext(response.data.hasNext);
        setPage(1);
        setSearch(searchValue);
      } finally {
        setLoading(false);
      }
    },
    []
  );

  const loadMore = useCallback(async () => {
    if (!hasNext || loading) {
      return;
    }

    setLoading(true);
    try {
      const nextPage = page + 1;
      const response = await fetchInvoices(nextPage, PAGE_SIZE, search);
      setRows((prev) => [...prev, ...response.data.data]);
      setHasNext(response.data.hasNext);
      setPage(nextPage);
    } finally {
      setLoading(false);
    }
  }, [hasNext, loading, page, search]);

  return {
    rows,
    hasNext,
    loading,
    search,
    setRows,
    resetAndLoad,
    loadMore
  };
}
