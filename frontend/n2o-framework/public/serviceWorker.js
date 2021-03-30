const CACHE = 'Hestia';
const ALLOWED_MASKS = [
  // .hash(.chunk)?.ext
  /(\..{8})(\.chunk)?\.js$/i,
  /(\..{8})(\.chunk)?\.css$/i,
  /\.woff2$/i
];
const ALLOWED_LOCATION = [
  location.origin
];

self.addEventListener('fetch', (event) => {
  const { request } = event;
  if (
    chekLocation(request.url, ALLOWED_LOCATION) &&
    checkMask(request.url, ALLOWED_MASKS)
  ) {
    event.respondWith(cacheFirst(CACHE, event.request));
  }
});

/**
 * Проверка адреса запроса на соответствие заданым маскам
 * @param {string} url Адрес запроса
 * @param {RegExp[]} masks
 * @return {boolean}
 */
function checkMask(url, masks) {
  return masks.some(mask => {
    return mask.test(url);
  });
}

/**
 * Проверка адреса запроса на соответствие заданым доменам
 * @param {string} url Адрес запроса
 * @param {string[]} locations Список доступных для кеширования доменов
 * @return {boolean}
 */
function chekLocation(url, locations) {
  return locations.some(loc => {
    return url.startsWith(loc);
  });
}

/**
 * Получение данных из кеша
 * @param {string} cacheName
 * @param {Request} request
 * @return {Promise<Response>}
 */
function fromCache(cacheName, request) {
  return caches.open(cacheName).then((cache) => {
    return cache.match(request).then((matching) => {
      return matching || Promise.reject('no-match');
    });
  });
}

/**
 * Обновление записи в кеше
 * @param {string} cacheName
 * @param {Request} request
 * @param {Response} response
 * @return {Promise<void>}
 */
function updateCache(cacheName, request, response) {
  return caches.open(cacheName).then((cache) => {
    return cache.put(request, response)
  });
}

/**
 * Поиск данных в кеше, либо запрос на сервер с последующим обновлением кеша
 * @param {string} cacheName
 * @param {Request} request
 * @return {Promise<Response>}
 */
function cacheFirst(cacheName, request) {
  return fromCache(cacheName, request).catch(() => {
    return fetch(request).then((response) => {
      // отдаём результат дальше, не дожидаясь записи в кеш
      updateCache(cacheName, request, response.clone());
      return response;
    });
  })
}
