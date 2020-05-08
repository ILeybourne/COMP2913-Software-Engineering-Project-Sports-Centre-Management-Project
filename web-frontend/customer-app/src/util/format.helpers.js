export const addZero = value => ("0" + value).slice(-2);

export const formatDate = value => {
  if (value) {
    const dt = new Date(value);
    return `${addZero(dt.getHours())}:${addZero(dt.getMinutes())}`;
  }
  return "";
};
export const dateToString = value => {
  const newDate = new Date(value);
  return (
    newDate.getDate() +
    "/" +
    (newDate.getMonth() + 1) +
    "/" +
    newDate.getFullYear()
  );
};

export const formatCurrency = value => {
  return "£" + Number(value).toFixed(2);
};
