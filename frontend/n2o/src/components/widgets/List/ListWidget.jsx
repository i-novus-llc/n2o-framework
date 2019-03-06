import React from 'react';
import { values } from 'lodash';
import { compose } from 'recompose';
import PropTypes from 'prop-types';
import dependency from '../../../core/dependency';
import StandardWidget from '../StandardWidget';
import ListContainer from './ListContainer';
import Fieldsets from '../Form/fieldsets';
import Pagination from '../Table/TablePagination';

function ListWidget(
  {
    id: widgetId,
    toolbar,
    disabled,
    actions,
    pageId,
    paging,
    className,
    style,
    filter,
    dataProvider,
    fetchOnInit,
    list,
    rowClick,
    hasMoreButton,
    maxHeight,
    maxWidth
  },
  context
) {
  const prepareFilters = () => {
    return context.resolveProps(filter, Fieldsets.StandardFieldset);
  };

  const resolveSections = () => {
    return context.resolveProps(list);
  };

  return (
    <StandardWidget
      disabled={disabled}
      widgetId={widgetId}
      toolbar={toolbar}
      actions={actions}
      filter={prepareFilters()}
      bottomLeft={paging && <Pagination widgetId={widgetId} />}
    >
      <ListContainer
        page={1}
        maxWidth={maxWidth}
        maxHeight={maxHeight}
        pageId={pageId}
        hasMoreButton={hasMoreButton}
        list={resolveSections()}
        disabled={disabled}
        dataProvider={dataProvider}
        widgetId={widgetId}
        fetchOnInit={fetchOnInit}
        actions={actions}
        rowClick={rowClick}
      />
    </StandardWidget>
  );
}

ListWidget.propTypes = {
  rowClick: PropTypes.func,
  hasMoreButton: PropTypes.bool,
  maxHeight: PropTypes.number,
  maxWidth: PropTypes.number
};
ListWidget.defaultProps = {
  rowClick: null,
  hasMoreButton: false
};
ListWidget.contextTypes = {
  resolveProps: PropTypes.func
};

export default compose(dependency)(ListWidget);
