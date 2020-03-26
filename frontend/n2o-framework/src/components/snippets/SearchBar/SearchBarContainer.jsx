import React from 'react';

import { connect } from 'react-redux';
import widgetContainer from 'n2o-framework/lib/components/widgets/WidgetContainer';

import { compose } from 'recompose';

import PropTypes from 'prop-types';

import split from 'lodash/split';
import map from 'lodash/map';
import every from 'lodash/every';
import get from 'lodash/get';
import filter from 'lodash/filter';
import isUndefined from 'lodash/isUndefined';

import withFetchData from '../../controls/withFetchData';
import dependency from '../../../core/dependency';
import SearchBar from './SearchBar';
import { WIDGETS } from '../../../core/factory/factoryLevels';

// const searchBarTrigger = () => {
//   return this.props.dataProvider
//     ? 'CHANGE'
//     : this.props.searchPageLocation
//     ? 'ENTER'
//     : 'CHANGE';
// };

function SearchBarContainer(props) {
  console.warn(props);
  return <SearchBar {...props} />;
}

export default compose(
  widgetContainer({
    mapProps: props => {
      return {
        datasource: props.datasource,
      };
    },
  })
)(withFetchData(SearchBarContainer));

// withFetchData(SearchBarContainer);
