import React from 'react';
import { storiesOf } from '@storybook/react';

import Icon from './Icon';

const stories = storiesOf('UI Компоненты/Иконка', module);

stories.add(
  'Использование иконок',
  () => {
    return (
      <React.Fragment>
        <div
          className="row"
          style={{
            marginBottom: '4px',
            display: 'flex',
            alignItems: 'flex-end',
          }}
        >
          <div className="col-md-2">
            <Icon name="fa fa-apple" />
          </div>
          <div className="col-md-2">
            <span className="text-primary">
              <Icon name="fa fa-2x fa-github" />
            </span>
          </div>
          <div className="col-md-2">
            <span className="text-success">
              <Icon name="fa fa-3x fa-android" />
            </span>
          </div>
          <div className="col-md-2">
            <span className="text-danger">
              <Icon name="fa fa-4x fa-telegram" />
            </span>
          </div>
        </div>
        <div className="row" style={{ marginBottom: '10px' }}>
          <div className="col-md-2">
            <h5>
              Apple{' '}
              <span className="label label-default">
                <Icon name="fa fa-apple" />
              </span>
            </h5>
          </div>
          <div className="col-md-2">
            <h5>
              Github{' '}
              <span className="label label-primary">
                <Icon name="fa fa-github" />
              </span>
            </h5>
          </div>
          <div className="col-md-2">
            <h5>
              Android{' '}
              <span className="label label-success">
                <Icon name="fa fa-android" />
              </span>
            </h5>
          </div>
          <div className="col-md-2">
            <h5>
              Telegram{' '}
              <span className="label label-danger">
                <Icon name="fa fa-telegram" />
              </span>
            </h5>
          </div>
        </div>
        <div className="row" style={{ marginBottom: '15px' }}>
          <div className="col-md-2">
            <button type="button" className="btn btn-default">
              <span>Apple</span>
              <Icon name="fa fa-apple" />
            </button>
          </div>
          <div className="col-md-2">
            <button type="button" className="btn btn-primary">
              <span>Github</span>
              <Icon name="fa fa-github" />
            </button>
          </div>
          <div className="col-md-2">
            <button type="button" className="btn btn-success">
              <span>Android</span>
              <Icon name="fa fa-android" />
            </button>
          </div>
          <div className="col-md-2">
            <button type="button" className="btn btn-danger">
              <span>Telegram</span>
              <Icon name="fa fa-telegram" />
            </button>
          </div>
        </div>
        <div className="row">
          <div className="col-md-3">
            <div className="alert alert-warning" role="alert">
              <Icon name="fa fa-apple" />
              Apple
            </div>
          </div>
          <div className="col-md-3">
            <div className="alert alert-info" role="alert">
              <Icon name="fa fa-github" />
              Github
            </div>
          </div>
          <div className="col-md-3">
            <div className="alert alert-success" role="alert">
              <Icon name="fa fa-android" />
              Android
            </div>
          </div>
          <div className="col-md-3">
            <div className="alert alert-danger" role="alert">
              <Icon name="fa fa-telegram" />
              Telegram
            </div>
          </div>
        </div>
      </React.Fragment>
    );
  },
  {
    info: {
      text: `
      Компонент 'Иконка'
      ~~~js
      import Icon from 'n2o-framework/lib/components/snippets/Icon/Icon';
      
        <div
          className="row"
          style={{
            marginBottom: '4px',
            display: 'flex',
            alignItems: 'flex-end',
          }}
        >
          <div className="col-md-2">
            <Icon name="fa fa-apple" />
          </div>
          <div className="col-md-2">
            <span className="text-primary">
              <Icon name="fa fa-2x fa-github" />
            </span>
          </div>
          <div className="col-md-2">
            <span className="text-success">
              <Icon name="fa fa-3x fa-android" />
            </span>
          </div>
          <div className="col-md-2">
            <span className="text-danger">
              <Icon name="fa fa-4x fa-telegram" />
            </span>
          </div>
        </div>
        <div className="row" style={{ marginBottom: '10px' }}>
          <div className="col-md-2">
            <h5>
              Apple{' '}
              <span className="label label-default">
                <Icon name="fa fa-apple" />
              </span>
            </h5>
          </div>
          <div className="col-md-2">
            <h5>
              Github{' '}
              <span className="label label-primary">
                <Icon name="fa fa-github" />
              </span>
            </h5>
          </div>
          <div className="col-md-2">
            <h5>
              Android{' '}
              <span className="label label-success">
                <Icon name="fa fa-android" />
              </span>
            </h5>
          </div>
          <div className="col-md-2">
            <h5>
              Telegram{' '}
              <span className="label label-danger">
                <Icon name="fa fa-telegram" />
              </span>
            </h5>
          </div>
        </div>
        <div className="row" style={{ marginBottom: '15px' }}>
          <div className="col-md-2">
            <button type="button" className="btn btn-default">
              <span>Apple</span>
              <Icon name="fa fa-apple" />
            </button>
          </div>
          <div className="col-md-2">
            <button type="button" className="btn btn-primary">
              <span>Github</span>
              <Icon name="fa fa-github" />
            </button>
          </div>
          <div className="col-md-2">
            <button type="button" className="btn btn-success">
              <span>Android</span>
              <Icon name="fa fa-android" />
            </button>
          </div>
          <div className="col-md-2">
            <button type="button" className="btn btn-danger">
              <span>Telegram</span>
              <Icon name="fa fa-telegram" />
            </button>
          </div>
        </div>
        <div className="row">
          <div className="col-md-3">
            <div className="alert alert-warning" role="alert">
              <Icon name="fa fa-apple" />
              Apple
            </div>
          </div>
          <div className="col-md-3">
            <div className="alert alert-info" role="alert">
              <Icon name="fa fa-github" />
              Github
            </div>
          </div>
          <div className="col-md-3">
            <div className="alert alert-success" role="alert">
              <Icon name="fa fa-android" />
              Android
            </div>
          </div>
          <div className="col-md-3">
            <div className="alert alert-danger" role="alert">
              <Icon name="fa fa-telegram" />
              Telegram
            </div>
          </div>
        </div>
      ~~~
      `,
    },
  }
);
