import React from 'react';
import PropTypes from 'prop-types';
import StandardWidget from 'n2o-framework/lib/components/widgets/StandardWidget';
import dependency from 'n2o-framework/lib/core/dependency';
import ToDoContainer from './ToDoContainer';

class ToDoWidget extends React.Component {
  /**
   * Мэппинг пропсов
   */
  getWidgetProps() {
    const { toolbar, actions, dataProvider } = this.props;
    return {
      toolbar,
      actions,
      dataProvider
    };
  }

  /**
   * Базовый рендер
   */
  render() {
    const {
      id: widgetId,
      toolbar,
      disabled,
      actions,
      pageId,
      paging,
      className,
      style
    } = this.props;
    return (
      <StandardWidget
        disabled={disabled}
        widgetId={widgetId}
        toolbar={toolbar}
        actions={actions}
        className={className}
        style={style}
      >
        <ToDoContainer
          widgetId={widgetId}
          size={10}
          page={1}
          {...this.getWidgetProps()}
        />
      </StandardWidget>
    );
  }
}

ToDoWidget.defaultProps = {
  toolbar: {}
};

ToDoWidget.propTypes = {
  containerId: PropTypes.string.isRequired,
  pageId: PropTypes.string.isRequired,
  url: PropTypes.bool,
  widgetId: PropTypes.string,
  toolbar: PropTypes.object,
  actions: PropTypes.object,
  html: PropTypes.string,
  dataProvider: PropTypes.object
};

export default dependency(ToDoWidget);
