/**
 * Created by emamoshin on 08.09.2017.
 */
import React from 'react';
import PropTypes from 'prop-types';
import 'whatwg-fetch';

import Spinner from '../../../components/snippets/Spinner/InlineSpinner';

/**
 * Компонент встаквки html-кода
 * @reactProps {string} url - url html, который будет вставляться
 * @reactProps {string} id - id виджета
 * @reactProps {string} html - html строка
 * @example
 * <Html id="HtmlWidget"
 *       url="/test.html"/>
 */
class Html extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      html: props.html || null,
      loading: false,
    };
  }

  static getDerivedStateFromProps(nextProps, prevState) {
    return {
      html: prevState.html || nextProps.html,
    };
  }

  fetch() {
    const { url } = this.props;
    return fetch(url);
  }

  /**
   * Фетчинг, если ошибка, то вызывает sendAlert
   * @returns (Promise) - возвращает промис с html
   */
  handleResponse() {
    return this.fetch().then(response => {
      if (response.status < 200 || response.status >= 300) {
        this.sendAlert();
      }
      return response.text();
    });
  }

  /**
   * Отправляет сообщение об ошибке
   */
  sendAlert() {
    const { url, id } = this.props;
    if (id) {
      //todo: добавить alers
    }
  }

  getData() {
    this.handleResponse()
      .then(html => {
        this.setState({
          loading: false,
          html,
        });
      })
      .catch(() => this.sendAlert());
  }

  /**
   * Вызывает handleResponse, обработка реджекта и резолва
   */
  componentDidMount() {
    if (this.props.url) {
      this.setState({ loading: true }, () => this.getData());
    }
  }

  /**
   * Базовый рендер
   */

  render() {
    const { loading, html } = this.state;

    return (
      <React.Fragment>
        {loading ? (
          <Spinner />
        ) : (
          <div dangerouslySetInnerHTML={{ __html: html }} />
        )}
      </React.Fragment>
    );
  }
}

Html.propTypes = {
  url: PropTypes.string,
  id: PropTypes.string,
  html: PropTypes.string,
};

export default Html;
