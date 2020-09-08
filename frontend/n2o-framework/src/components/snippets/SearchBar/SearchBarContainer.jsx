import React from 'react';
import PropTypes from 'prop-types';

import isUndefined from 'lodash/isUndefined';

import { compose, lifecycle } from 'recompose';

import listContainer from '../../controls/listContainer';

import SearchBar from './SearchBar';

function SearchBarContainer(props) {
  const {
    data,
    dataProvider,
    searchPageLocation,
    onSearch,
    button,
    icon,
    directionIconsInPopUp,
  } = props;

  const currentTrigger = !isUndefined(dataProvider) ? 'CHANGE' : 'ENTER';

  return (
    <SearchBar
      menu={data}
      trigger={currentTrigger}
      onSearch={onSearch}
      button={button}
      icon={icon}
      directionIconsInPopUp={directionIconsInPopUp}
    />
  );
}

SearchBarContainer.defaultProps = {
  button: false,
  icon: 'fa fa-search',
  directionIconsInPopUp: 'left',
  onSearch: () => {},
  data: [],
};

SearchBarContainer.propTypes = {
  /**
   * данные в popUp(ссылки), при наличии dataProvider
   */
  data: PropTypes.array,
  /**
   * Направление иконок ссылок в popUp
   */
  directionIconsInPopUp: PropTypes.string,
};

export default compose(
  listContainer,
  lifecycle({
    componentDidMount() {
      const { _fetchData } = this.props;
      _fetchData();
    },
  })
)(SearchBarContainer);
