import 'whatwg-fetch';

export default function request() {
  return fetch('/data/config').then(function(response) {
    return response.json();
  });
}
