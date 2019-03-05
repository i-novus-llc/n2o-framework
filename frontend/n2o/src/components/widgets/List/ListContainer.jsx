import React from 'react';
import { compose } from 'recompose';
import widgetContainer from '../WidgetContainer';
import List from './List';

class ListContainer extends React.Component {
  constructor(props) {
    super(props);

    this.getWidgetProps = this.getWidgetProps.bind(this);
  }

  getWidgetProps() {
    const {} = this.props;

    return {};
  }
  render() {
    return <List {...this.getWidgetProps()} />;
  }
}

ListContainer.propTypes = {};

export default compose(
  widgetContainer(
    {
      mapProps: props => ({
        ...props
      })
    },
    'ListWidget'
  )
)(ListContainer);
