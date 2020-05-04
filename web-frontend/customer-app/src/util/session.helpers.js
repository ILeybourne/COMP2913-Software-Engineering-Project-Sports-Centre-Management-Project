export const sessionToEvent = activity => {
  return {
    id: activity.id,
    resourceId: activity.resource.id,
    title: activity.name,
    start: activity.startTime,
    end: activity.endTime,
    totalCapacity: activity.totalCapacity,
    currentCapacity: activity.currentCapacity
  };
};

export const isEmpty = obj => {
  return Object.keys(obj).length === 0;
};
