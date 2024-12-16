import React from 'react';
import config from '../config';

const ProductCard = ({ product }) => {
  return (
    <div className="product-card">
      <div className="product-image">
        <img 
          src={config.getImageUrl(product.imageUrl)} 
          alt={product.name}
          style={{
            maxWidth: '100%',
            height: 'auto',
            border: '1px solid #ddd',
            borderRadius: '4px'
          }}
          onError={(e) => {
            console.error('图片加载失败:', e.target.src);
            // 可以设置一个默认图片
            e.target.src = '/default-product-image.png';
          }}
        />
      </div>
      <div className="product-info">
        <h3>{product.name}</h3>
        <p>{product.description}</p>
        <p className="price">¥{product.price}</p>
      </div>
    </div>
  );
};

export default ProductCard; 