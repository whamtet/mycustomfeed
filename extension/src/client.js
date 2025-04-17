const BASE_URL = process.env.BASE_URL;

export function GET(URL) {
  return fetch(BASE_URL + URL);
}

export function POST(url) {
  return fetch(BASE_URL + url,
      {
        method: 'POST',
        headers: {
          'kookie': localStorage.getItem('kookie'),
        }
      }
  );
}
