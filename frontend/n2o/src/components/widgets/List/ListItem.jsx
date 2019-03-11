import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';

/**
 * Компонент ListItem виджета ListWidget
 * @param {Node|Object} image - секция картинки
 * @param {Node|String} header - секция заголовка
 * @param {Node|String} subHeader - секция подзаголовка
 * @param {Node|String} body - секция тела
 * @param {Node|String} rightTop - секция справа сверху
 * @param {Node|String} rightBottom - секция справа снизу
 * @param {Node|String} extra - extra секция
 * @param {boolean} selected - флаг активности строки
 * @param {function} onClick - callback на клик по строке
 * @returns {*}
 * @constructor
 */
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
        {rightBottom && <div className="n2o-widget-list-item-right-bottom">{rightBottom}</div>}
      </div>
      <div className="n2o-widget-list-item-extra-container">
        {extra && <div className="n2o-widget-list-item-extra">{extra}</div>}
      </div>
    </div>
  );
}

ListItem.propTypes = {
  image: PropTypes.oneOf(PropTypes.node, PropTypes.object),
  header: PropTypes.oneOf(PropTypes.node, PropTypes.string),
  subHeader: PropTypes.oneOf(PropTypes.node, PropTypes.string),
  body: PropTypes.oneOf(PropTypes.node, PropTypes.string),
  rightTop: PropTypes.oneOf(PropTypes.node, PropTypes.string),
  rightBottom: PropTypes.oneOf(PropTypes.node, PropTypes.string),
  extra: PropTypes.oneOf(PropTypes.node, PropTypes.string),
  selected: PropTypes.bool,
  onClick: PropTypes.func
};

export default ListItem;
