import React from 'react';
import { storiesOf } from '@storybook/react';
import { omit } from 'lodash';
import { Route, Switch } from 'react-router-dom';

import Link from './Link';
import MetaJson from './Link.meta.json';

const stories = storiesOf('Кнопки', module);

stories.add('Ссылка', () => (
  <div>
    <div className="d-flex align-items-center">
      <Link {...omit(MetaJson, ['conditions', 'confirm'])} />
      <div className="mr-4">
        <Link
          {...omit(MetaJson, ['conditions', 'target'])}
          label="Внутреняя ссылка"
          icon="fa fa-link"
          color="secondary"
          inner={true}
        />
      </div>
      <Switch>
        <Route
          path="*"
          render={props => (
            <React.Fragment>
              <div style={{ textAlign: 'right' }}>
                URL:{' '}
                <kbd>
                  {props.location.pathname}
                  {props.location.search}
                </kbd>
              </div>
            </React.Fragment>
          )}
        />
      </Switch>
    </div>
  </div>
));
