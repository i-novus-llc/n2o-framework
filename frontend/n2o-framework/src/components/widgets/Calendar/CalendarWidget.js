import React from 'react';
import { compose, withHandlers } from 'recompose';
import PropTypes from 'prop-types';

import dependency from '../../../core/dependency';
import CalendarContainer from './CalendarContainer';
import StandardWidget from '../StandardWidget';
import Fieldsets from '../Form/fieldsets';

function CalendarWidget({
  id: widgetId,
  toolbar,
  disabled,
  pageId,
  className,
  style,
  filter,
  dataProvider,
  fetchOnInit,
  calendar,
  prepareFilters,
  paging,
}) {
  const { size } = paging;

  return (
    <StandardWidget
      disabled={disabled}
      widgetId={widgetId}
      toolbar={toolbar}
      filter={prepareFilters()}
      className={className}
      style={style}
    >
      <CalendarContainer
        page={1}
        size={size}
        pageId={pageId}
        disabled={disabled}
        dataProvider={dataProvider}
        widgetId={widgetId}
        fetchOnInit={fetchOnInit}
        {...calendar}
      />
    </StandardWidget>
  );
}

CalendarWidget.propTypes = {
  widgetId: PropTypes.string,
  toolbar: PropTypes.object,
  disabled: PropTypes.bool,
  pageId: PropTypes.string,
  className: PropTypes.string,
  style: PropTypes.object,
  filter: PropTypes.object,
  dataProvider: PropTypes.object,
  fetchOnInit: PropTypes.bool,
};
CalendarWidget.defaultProps = {
  toolbar: {},
  disabled: false,
  filter: {},
  paging: {},
};
CalendarWidget.contextTypes = {
  resolveProps: PropTypes.func,
};

export default compose(
  dependency,
  withHandlers({
    prepareFilters: () => (context, filter) =>
      context.resolveProps(filter, Fieldsets.StandardFieldset),
  })
)(CalendarWidget);
