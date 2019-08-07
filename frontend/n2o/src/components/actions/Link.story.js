import React from 'react';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';

import { getStubData } from 'N2oStorybook/fetchMock';
import Actions from './Actions';
import metadata from './Link.meta';
import { Route, Switch } from 'react-router-dom';

const stories = storiesOf('Действия/Ссылка Link', module);

stories
  .addDecorator(jsxDecorator)
  .add('Компонент', () => {
    return (
      <Actions toolbar={metadata.toolbar} containerKey={'linkContainer'} />
    );
  })
  .add('Метаданные', () => {
    return (
      <React.Fragment>
        <Actions
          actions={metadata.actions}
          toolbar={metadata.toolbar}
          containerKey={'linkContainer'}
        />
        <Switch>
          <Route
            path="*"
            component={({ match }) =>
              match.url !== '/' && (
                <span>
                  Сработал роутер для: <pre>{match.url}</pre>
                </span>
              )
            }
          />
        </Switch>
      </React.Fragment>
    );
  });
