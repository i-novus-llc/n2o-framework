import React from 'react';
import PropTypes from 'prop-types';
import Spinner from '../../../components/snippets/Spinner/InlineSpinner';

/**
 * Компонент встаквки html-кода
 * @reactProps {string} url - url html, который будет вставляться
 * @reactProps {string} id - id виджета
 * @reactProps {string} html - html строка
 * @reactProps {object} data - данные
 * <Html id="HtmlWidget" url="/test.html"/>
 */

//Принимает html и data
// прим. html = <h1>User is :name :surname</h1> ,data = [{"name" : "testUserName", "surname": "testUserSurname"}],
// заменяет плейсхолдеры в html (прим. :name, :surname) на стоотвствующие значения по ключам в data, при resolvePlaceholders === true.

export const replacePlaceholders = (html, data) => {
  const keys = Object.keys(data);
  keys.forEach(key => {
    html = html.replace(new RegExp(':' + key, 'gm'), data[key]);
  });
  return html;
};

const Html = props => {
  const { html, loading, data, resolvePlaceholders } = props;

  return (
    <>
      {loading ? (
        <Spinner />
      ) : (
        <div
          dangerouslySetInnerHTML={{
            __html:
              resolvePlaceholders && data
                ? replacePlaceholders(html, data)
                : html,
          }}
        />
      )}
    </>
  );
};

Html.propTypes = {
  url: PropTypes.string,
  id: PropTypes.string,
  html: PropTypes.string,
  data: PropTypes.object,
};

export default Html;
