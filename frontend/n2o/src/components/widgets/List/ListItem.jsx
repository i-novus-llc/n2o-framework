import React from 'react';
import cn from 'classnames';

function ListItem({
  image,
  header,
  subHeader,
  body,
  rightTop,
  rightBottom,
  extra,
  selected,
  onClick
}) {
  const renderImage = image => {
    return React.isValidElement(image) ? (
      image
    ) : (
      <img src={image.src} alt={image.alt || ''} {...image} />
    );
  };

  return (
    <div
      onClick={onClick}
      className={cn('n2o-widget-list-item', {
        'n2o-widget-list-item--active': selected
      })}
    >
      <div className="n2o-widget-list-item-image-container">
        {image && <div className="n2o-widget-list-item-image">{renderImage(image)}</div>}
      </div>
      <div className="n2o-widget-list-item-main-container">
        {header && (
          <div className="n2o-widget-list-item-header">
            <h3>{header}</h3>
          </div>
        )}
        {subHeader && (
          <div className="n2o-widget-list-item-subheader text-muted">
            <h4>{subHeader}</h4>
          </div>
        )}
        {body && <div className="n2o-widget-list-item-body">{body}</div>}
      </div>
      <div className="n2o-widget-list-item-right-container">
        {rightTop && <div className="n2o-widget-list-item-right-top">{rightTop}</div>}
        {rightBottom && <div className="n2o-widget-list-item-right-top">{rightBottom}</div>}
      </div>
      <div className="n2o-widget-list-item-extra-container">
        {extra && <div className="n2o-widget-list-item-extra">{extra}</div>}
      </div>
    </div>
  );
}

export default ListItem;
