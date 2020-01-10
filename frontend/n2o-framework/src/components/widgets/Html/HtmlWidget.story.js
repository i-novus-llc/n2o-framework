import React from 'react';
import { storiesOf } from '@storybook/react';
import fetchMock from 'fetch-mock';
import HtmlWidgetJson from './HtmlWidget.meta';
import HtmlWidget from './HtmlWidget';

const stories = storiesOf('Виджеты/HtmlWidget', module);

stories.add(
  'Метаданные',
  () => {
    fetchMock.restore().get('/html-data?', url => {
      return {
        count: 1,
        size: 1,
        list: [
          {
            username: 'Афанасий',
            surname: 'Ревин',
            html: '<h5>custom html template</h5><input/>',
          },
        ],
      };
    });

    return (
      <HtmlWidget
        {...HtmlWidgetJson['widgets']['Page_Html']}
        id={'Page_Html'}
        pageId={'Page_Html'}
        resolvePlaceholders={false}
      />
    );
  },
  {
    info: {
      text: `
    Компонент 'HtmlWidget'
    ~~~js
    import HtmlWidget from 'n2o-framework/lib/components/widgets/Html/HtmlWidget';
    
    <Html id="Page_Html" html="" url="/html-data" />
    html : '<div class='jumbotron'><h4>user: :username :surname / username: :username / surname: :surname</h4></div><div class='jumbotron'><ul><li>:username</li><li>:surname</li></ul></div>'    

    ~~~
    `,
    },
  }
);
