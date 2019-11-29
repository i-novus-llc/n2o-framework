import React from 'react';
import widgetContainer from '../WidgetContainer';
import Html from './Html';
import { HTML } from '../widgetTypes';
import 'whatwg-fetch';

class HtmlContainer extends React.Component {
  state = {
    loading: false,
  };
  getHtmlProps = () => {
    const { html, datasource, resolvePlaceholders } = this.props;
    return {
      loading: this.state.loading,
      html: html || null,
      data: datasource,
      resolvePlaceholders: resolvePlaceholders,
    };
  };

  static getDerivedStateFromProps(nextProps, prevState) {
    return {
      html: prevState.html || nextProps.html,
    };
  }

  componentDidMount() {
    !this.props.html &&
      this.setState({
        loading: true,
      });
  }

  /**
   * Базовый рендер
   */

  render() {
    return <Html {...this.getHtmlProps()} {...this.props} />;
  }
}

export default widgetContainer(
  {
    mapProps: props => {
      return {
        ...props,
        datasource: props.datasource && props.datasource[0],
      };
    },
  },
  HTML
)(HtmlContainer);
