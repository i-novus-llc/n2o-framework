import React from 'react';

function ListItem({ image, header, subHeader, body, rightTop, rightBottom, extra }) {
  const renderImage = () => (
    <div className="n2o-widget-list-item-image">
      {image.component || <img src={image.src} alt={image.alt} {...image} />}
    </div>
  );

  const renderHeader = () => (
    <div className="n2o-widget-list-item-header">{header.component || <h3>{header.text}</h3>}</div>
  );

  return (
    <div className="n2o-widget-list-item">
      <div className="n2o-widget-list-item-image-container">{image && renderImage()}</div>
      <div className="n2o-widget-list-item-main-container">{header && renderHeader()}</div>
    </div>
  );
}

export default ListItem;
