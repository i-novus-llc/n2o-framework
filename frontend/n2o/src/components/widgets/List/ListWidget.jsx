import React from 'react';
import { compose } from 'recompose';
import PropTypes from 'prop-types';
import dependency from '../../../core/dependency';
import StandardWidget from '../StandardWidget';
import ListContainer from './ListContainer';
import Fieldsets from '../Form/fieldsets';

function ListWidget(
  { id: widgetId, toolbar, disabled, actions, pageId, paging, className, style, filter },
  context
) {
  const prepareFilters = () => {
    return context.resolveProps(filter, Fieldsets.StandardFieldset);
  };

  return (
    <StandardWidget
      disabled={disabled}
      widgetId={widgetId}
      toolbar={toolbar}
      actions={actions}
      filter={prepareFilters()}
    >
      <ListContainer />
    </StandardWidget>
  );
}

ListWidget.propTypes = {};
ListWidget.defaultProps = {};
ListWidget.contextTypes = {
  resolveProps: PropTypes.func
};

export default compose(dependency)(ListWidget);
