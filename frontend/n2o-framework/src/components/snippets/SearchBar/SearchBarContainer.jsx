import React from 'react';

import get from 'lodash/get';
import isUndefined from 'lodash/isUndefined';

import withFetchData from '../../controls/withFetchData';
import SearchBar from './SearchBar';

function SearchBarContainer(props) {
  const { data } = props;

  const searchConfig = get(data[0], 'menu.search');
  //если menu undefined SearchBar без PopUp
  const menu = get(searchConfig, 'dataProvider.list');

  const searchConfigHasKey = key => !isUndefined(get(searchConfig, key));
  //возвращает trigger из search в зависимости от данных
  const currentTrigger = () =>
    searchConfigHasKey('dataProvider')
      ? 'CHANGE'
      : searchConfigHasKey('searchPageLocation')
      ? 'ENTER'
      : 'CHANGE';

  return <SearchBar menu={menu} trigger={currentTrigger()} />;
}

export default withFetchData(SearchBarContainer);
