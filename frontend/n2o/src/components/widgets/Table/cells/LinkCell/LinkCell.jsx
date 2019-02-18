import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';
import { get } from 'lodash';
import { Button } from 'reactstrap';
import withCell from '../../withCell';
import { Link } from 'react-router-dom';
import { LinkType } from '../../../../../impl/linkTypes';
import { LinkCellType } from './linkCellTypes';

/**
 * Компонент LinkCell
 * @param id
 * @param fieldKey
 * @param className - класс
 * @param style - объект стилей
 * @param model - модель
 * @param callActionImpl - экшен на клик
 * @param icon - класс иконки
 * @param type
 * @param url - при наличии этого параметра, в зависимости от
 * параметра target будет создана ссылка с соответствующим таргетом,
 * при отсутствии, компонент будет вызывать приходящий экшен
 * @param target - тип ссылки
 * @param rest
 * @param visible - флаг видимости
 * @returns {*}
 * @constructor
 */
function LinkCell({
  id,
  fieldKey,
  className,
  style,
  model,
  callActionImpl,
  icon,
  type,
  url,
  target,
  visible,
  ...rest
}) {
  const props = {
    style,
    className: cn('n2o-link-cell', 'p-0', { [className]: className })
  };

  const handleClick = e => {
    callActionImpl(e, {});
  };

  const getLinkContent = () => {
    return (
      <React.Fragment>
        {icon &&
          (type === LinkCellType.ICON || type === LinkCellType.ICONANDTEXT) && (
            <i style={{ marginRight: 5 }} className={icon} />
          )}
        {(type === LinkCellType.ICONANDTEXT || type === LinkCellType.TEXT) &&
          get(model, fieldKey || id)}
      </React.Fragment>
    );
  };

  const ActionLinkCell = () => {
    return (
      <Button color="link" onClick={handleClick} {...props}>
        {getLinkContent()}
      </Button>
    );
  };

  const UrlLinkCell = () => {
    return (
      <React.Fragment>
        {target === LinkType.APPLICATION && (
          <Link to={url} {...props}>
            {getLinkContent()}
          </Link>
        )}
        {(target === LinkType.SELF || target === LinkType.BLANK) && (
          <a href={url} target={target === LinkType.BLANK ? '_blank' : ''} {...props}>
            {getLinkContent()}
          </a>
        )}
      </React.Fragment>
    );
  };
  return visible && (url ? <UrlLinkCell /> : <ActionLinkCell />);
}

LinkCell.propTypes = {
  icon: PropTypes.string,
  type: PropTypes.string,
  id: PropTypes.string,
  fieldKey: PropTypes.string,
  model: PropTypes.object.dependencies,
  visible: PropTypes.bool,
  url: PropTypes.string,
  target: PropTypes.string
};

LinkCell.defaultProps = {
  type: LinkCellType.TEXT,
  visible: true
};

export default withCell(LinkCell);
