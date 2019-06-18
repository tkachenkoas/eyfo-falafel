export function logAndReturn<T>(data: T, type: string): T {
  console.log(`${type}: ${JSON.stringify(data)}`);
  return data;
}
