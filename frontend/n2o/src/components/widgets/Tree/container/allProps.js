import PropTypes from 'prop-types';

export const defaultProps = {
  disabled: false,
  loading: false,
  datasource: [],
  parentIcon: '',
  childIcon: '',
  multiselect: true,
  showLine: false,
  filter: '',
  expandBtn: true
};

export const propTypes = {
  disabled: PropTypes.bool,
  loading: PropTypes.bool,
  datasource: PropTypes.array,
  parentIcon: PropTypes.string,
  childIcon: PropTypes.string,
  multiple: PropTypes.bool,
  showLine: PropTypes.bool,
  filter: PropTypes.string,
  expandBtn: PropTypes.bool
};
