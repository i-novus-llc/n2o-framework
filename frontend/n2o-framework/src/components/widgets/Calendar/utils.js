export function isDayOff(day) {
  return [0, 6].indexOf(day.getDay()) !== -1;
}

export function isCurrentDay(day) {
  return day.getDay() === new Date().getDay();
}
