/*
 * Временный дубликат файла "frontend\n2o-framework\public\serviceWorker.js"
 * TODO удалить после того как научим сборщик класть внешний файл рядом с основным бандлом, не включаяя в него
 */
const CACHE="Hestia",ALLOWED_MASKS=[/(\-.{8})\.js$/i,/(\-.{8})\.css$/i,/(\-.{8})\.svg$/i,/\.woff2$/i],ALLOWED_LOCATION=[location.origin];function checkMask(e,t){return t.some(t=>t.test(e))}function chekLocation(e,t){return t.some(t=>e.startsWith(t))}function fromCache(e,t){return caches.open(e).then(e=>e.match(t).then(e=>e||Promise.reject("no-match")))}function updateCache(e,t,c){return caches.open(e).then(e=>e.put(t,c))}function cacheFirst(e,t){return fromCache(e,t).catch(()=>fetch(t).then(c=>(updateCache(e,t,c.clone()),c)))}self.addEventListener("fetch",e=>{const{request:t}=e;chekLocation(t.url,ALLOWED_LOCATION)&&checkMask(t.url,ALLOWED_MASKS)&&e.respondWith(cacheFirst(CACHE,e.request))});
