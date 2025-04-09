const BASE_URL = process.env.BASE_URL;

export function GET(url) {
  return fetch(BASE_URL + url);
}
