import React from 'react';
import isEmpty from 'lodash/isEmpty';
import cn from 'classnames';
import { compose, withHandlers, withProps } from 'recompose';
import get from 'lodash/get';
import { batchActions } from 'redux-batched-actions';

import { updateModel } from '../../../actions/models';
import { dataRequestWidget } from '../../../actions/widgets';
import Alert from '../../snippets/Alerts/Alert';
import DocumentTitle from '../../core/DocumentTitle';
import DefaultBreadcrumb from '../../core/Breadcrumb/DefaultBreadcrumb';
import BreadcrumbContainer from '../../core/Breadcrumb/BreadcrumbContainer';
import Actions from '../../actions/Actions';
import PageRegions from '../PageRegions';
import SearchBar from '../../snippets/SearchBar/SearchBar';

function SearchablePage({
  id,
  metadata,
  toolbar,
  actions,
  containerKey,
  error,
  pageId,
  regions,
  disabled,
  onSearch,
  searchBar = {},
}) {
  return (
    <div
      classname={cn('n2o-page n2o-page__searchable-page n2o-searchable-page', {
        'n2o-disabled-page': disabled,
      })}
    >
      {error && <Alert {...error} visible />}
      {!isEmpty(metadata) && metadata.page && (
        <DocumentTitle {...metadata.page} />
      )}
      <div className="n2o-searchable-page__title d-flex align-items-baseline">
        {!isEmpty(metadata) && metadata.breadcrumb && (
          <BreadcrumbContainer
            defaultBreadcrumb={DefaultBreadcrumb}
            items={metadata.breadcrumb}
          />
        )}
        <Actions
          className="ml-3"
          toolbar={toolbar}
          actions={actions}
          containerKey={containerKey}
          pageId={pageId}
        />
        <SearchBar
          {...searchBar}
          className={cn('ml-auto', searchBar.className)}
          onSearch={onSearch}
        />
      </div>
      <PageRegions id={id} regions={regions} />
    </div>
  );
}

const enhance = compose(
  withProps(props => ({
    searchWidgetId: get(props, 'metadata.searchWidgetId'),
    searchModelPrefix: get(props, 'metadata.searchModelPrefix'),
    searchModelKey: get(props, 'metadata.searchModelKey'),
  })),
  withHandlers({
    onSearch: ({
      dispatch,
      searchWidgetId,
      searchModelPrefix,
      searchModelKey,
    }) => value => {
      dispatch(
        batchActions([
          updateModel(searchModelPrefix, searchWidgetId, searchModelKey, value),
          dataRequestWidget(searchWidgetId),
        ])
      );
    },
  })
);

export { SearchablePage };
export default enhance(SearchablePage);
