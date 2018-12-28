const Size = {
  0: 'Б',
  1: 'КБ',
  2: 'МБ'
};

export function post(url, file, onProgress, onUpload) {
  const xhr = new XMLHttpRequest();
  xhr.upload.onprogress = onProgress;
  xhr.onreadystatechange = () => onUpload(xhr);
  xhr.open('POST', url, true);
  xhr.send(file);
  return xhr;
}

export function deleteFile(url, id) {
  const xhr = new XMLHttpRequest();
  xhr.open('DELETE', url + `/${id}`, true);
  xhr.send();
}

export function convertSize(size, step = 0) {
  if (!size || size === 0) {
    return ' 0Б';
  }
  if (size / 1024 > 1) {
    return convertSize(size / 1024, step + 1);
  }
  return Math.round(size) + ' ' + Size[step];
}
