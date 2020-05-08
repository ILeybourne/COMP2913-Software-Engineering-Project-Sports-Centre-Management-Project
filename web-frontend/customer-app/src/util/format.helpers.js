export const addZero = value => ("0" + value).slice(-2);

export const formatDate = value => {
  if (value) {
    const dt = new Date(value);
    return `${addZero(dt.getHours())}:${addZero(dt.getMinutes())}`;
  }
  return "";
};

export const formatCurrency = value => {
  return "£" + Number(value).toFixed(2);
};

export const formatDateYMD = value => {
  if (value) {
    const dt = new Date(value);
    return dt.getFullYear() + "-" + dt.getMonth() + "-" + dt.getDate();
  }
  return "";
}