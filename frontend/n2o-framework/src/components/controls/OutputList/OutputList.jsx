import React from 'react';
import PropTypes from 'prop-types';

import map from 'lodash/map';

import cn from 'classnames';

import { Link } from 'react-router-dom';

import { getHref, getLabel, hasLink, lastItem } from './utils';

function OutputList({
  value,
  className,
  labelFieldId,
  linkFieldId,
  target,
  direction,
  separator,
}) {
  const directionClassName = `n2o-output-list_${direction}_direction`;

  return (
    <div
      className={cn('n2o-output-list', directionClassName, {
        [className]: className,
      })}
    >
      {map(value, (item, index) => {
        const label = (
          <>
            {getLabel(item, labelFieldId) +
              `${!lastItem(value, index) ? separator : ''}`}
          </>
        );
        const href = getHref(item, linkFieldId);
        const link = hasLink(item, linkFieldId);

        return link ? (
          <Link to={href} target={target} className="n2o-output-list__link">
            {label}
          </Link>
        ) : (
          <span className="n2o-output-list__text">{label}</span>
        );
      })}
    </div>
  );
}

OutputList.propTypes = {
  /**
   * элементы OutputList, строки или ссылки
   */
  value: PropTypes.array,
  /**
   * кастомный класс контейнера
   */
  className: PropTypes.string,
  /**
   * id по которому из value берется текст row или link
   */
  labelFieldId: PropTypes.string,
  /**
   * id по которому из value берется href для link
   */
  linkFieldId: PropTypes.string,
  /**
   * Тип ссылки
   */
  target: PropTypes.string,
  /**
   * направление OutputList. row - элементы в строку. column(default) - элементы списком
   */
  direction: PropTypes.string,
  /**
   * разделитель между элементами (space default)
   */
  separator: PropTypes.string,
};

OutputList.defaultProps = {
  value: [],
  labelFieldId: 'name',
  linkFieldId: 'href',
  target: '_blank',
  direction: 'column',
  separator: ' ',
};

export default OutputList;
