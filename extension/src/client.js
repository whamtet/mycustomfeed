export function GET(URL) {
  return fetch(BASE_URL + URL);
}

export async function POST(url) {
  const response = await fetch(BASE_URL + url,
      {
        method: 'POST',
        headers: {
          'kookie': localStorage.getItem('kookie'),
        }
      }
  );
  const kookie = response.headers.get('kookie');
  if (kookie) {
      localStorage.setItem('kookie', kookie);
  }
  return response;
}
