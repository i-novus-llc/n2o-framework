import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import cx from 'classnames';
import Text from '../../snippets/Text/Text';
import Icon from '../../snippets/Icon/Icon';

const TypesComponents = {
  icon: ({ icon }) => <Icon className="icon" name={icon} />,
  text: ({ value, format }) => (
    <Text className="text" text={value} format={format} />
  ),
  iconAndText: ({ icon, value, format }) => (
    <Fragment>
      {icon && <Icon className="icon" name={icon} />}
      <Text className="text" text={value} format={format} />
    </Fragment>
  ),
};

/**
 * Компонент OutPutText
 * @reactProps {boolean} disabled
 * @reactProps {string} className
 * @reactProps {object} style
 * @reactProps {string} type
 * @reactProps {string} textPlace
 * @reactProps {string} icon
 * @reactProps {string} value
 * @reactProps {string} format
 */
function OutPutText({ textPlace, type, className, style, ...rest }) {
  const RenderComponent = TypesComponents[type];

  return (
    <div className={cx('n2o-output-text', className, textPlace)} style={style}>
      <RenderComponent {...rest} />
    </div>
  );
}

OutPutText.propTypes = {
  disabled: PropTypes.bool,
  className: PropTypes.string,
  style: PropTypes.object,
  type: PropTypes.oneOf(Object.keys(TypesComponents)),
  textPlace: PropTypes.oneOf(['right', 'left']),
  icon: PropTypes.string,
  value: PropTypes.string,
  format: PropTypes.string,
};

OutPutText.defaultProps = {
  disabled: true,
  className: 'n2o',
  style: {},
  type: 'iconAndText',
  textPlace: 'left',
};

export default OutPutText;
