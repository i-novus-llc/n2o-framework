const cachingStore = () => {
  const cache = {};

  return {
    add: function(params, result) {
      cache[JSON.stringify(params)] = result;
    },

    find: function(params) {
      const key = JSON.stringify(params);

      if (cache.hasOwnProperty(key)) {
        return cache[key];
      } else {
        return false;
      }
    },
  };
};

export default cachingStore();
