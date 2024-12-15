module.exports = {
  devServer: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        ws: true,
        timeout: 30000,
        onError: (err, req, res) => {
          console.error('Proxy error:', err);
        },
        onProxyReq: (proxyReq, req, res) => {
          console.log('Proxy request:', req.method, req.url);
        },
        onProxyRes: (proxyRes, req, res) => {
          console.log('Proxy response:', proxyRes.statusCode);
        }
      }
    }
  }
}