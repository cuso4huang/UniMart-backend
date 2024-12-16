const config = {
  API_BASE_URL: process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080',
  UPLOAD_PATH: '/uploads/',
  getImageUrl: (filename) => {
    if (!filename) return '';
    if (filename.startsWith('http')) return filename;
    return `${config.API_BASE_URL}${config.UPLOAD_PATH}${filename}`;
  }
};

export default config; 