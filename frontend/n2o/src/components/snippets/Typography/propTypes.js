import PropTypes from 'prop-types';

export const propTypes = {
  code: PropTypes.bool,
  del: PropTypes.bool,
  mark: PropTypes.bool,
  strong: PropTypes.bool,
  underline: PropTypes.bool,
  small: PropTypes.bool,
  text: PropTypes.string,
  children: PropTypes.node,
  onChange: PropTypes.func,
  color: PropTypes.string,
};

export const defaultProps = {
  code: false,
  del: false,
  mark: false,
  strong: false,
  underline: false,
  small: false,
  text: '',
  onChange: () => {},
  color: '',
};
