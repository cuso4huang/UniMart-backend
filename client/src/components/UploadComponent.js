import React, { useState } from 'react';
import config from '../config';

const UploadComponent = () => {
  const [imageUrl, setImageUrl] = useState('');
  const [previewUrl, setPreviewUrl] = useState('');

  const handleUpload = async (file) => {
    try {
      const localPreviewUrl = URL.createObjectURL(file);
      setPreviewUrl(localPreviewUrl);

      const formData = new FormData();
      formData.append('file', file);

      const response = await fetch('/api/upload', {
        method: 'POST',
        body: formData
      });

      if (!response.ok) {
        throw new Error('上传失败');
      }

      const data = await response.json();
      console.log('上传响应:', data);
      
      const fullUrl = config.getImageUrl(data.filename);
      setImageUrl(fullUrl);
      console.log('设置的图片URL:', fullUrl);
    } catch (error) {
      console.error('上传错误:', error);
    }
  };

  const cleanupPreview = () => {
    if (previewUrl) {
      URL.revokeObjectURL(previewUrl);
    }
  };

  React.useEffect(() => {
    return () => cleanupPreview();
  }, []);

  return (
    <div>
      <input 
        type="file" 
        accept="image/*" 
        onChange={(e) => {
          const file = e.target.files[0];
          if (file) {
            cleanupPreview();
            handleUpload(file);
          }
        }} 
      />
      {(previewUrl || imageUrl) && (
        <div>
          <p>图片URL: {imageUrl || '正在上传...'}</p>
          <img 
            src={previewUrl || imageUrl} 
            alt="上传的图片"
            style={{
              maxWidth: '300px',
              border: '1px solid #ddd',
              borderRadius: '4px',
              padding: '5px'
            }}
            onError={(e) => {
              console.error('图片加载失败:', e);
              console.log('当前图片URL:', imageUrl);
              e.target.style.display = 'none';
            }}
          />
        </div>
      )}
    </div>
  );
};

export default UploadComponent;