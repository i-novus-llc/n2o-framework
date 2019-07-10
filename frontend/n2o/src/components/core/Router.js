import React from 'react';
import PropTypes from 'prop-types';
import { Switch } from 'react-router-dom';
import { ConnectedRouter } from 'connected-react-router';
import { pure } from 'recompose';
import history from '../../history';
import Root from './Root';
import Route from './Route';
import Page from './Page';

const errorStyle = {
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  flexDirection: 'column',
  width: '100%',
  height: '100%',
  fontSize: '1.25rem',
  textAlign: 'center',
};

function Router({ embeddedRouting, children }) {
  if (!embeddedRouting && !React.Children.count(children)) {
    return (
      <div style={errorStyle}>
        <p>&#9888; Страницы не настроены.</p>
        <p>
          Нужно добавить <code>&lt;Page&gt;</code> в компонент
          <code>&lt;N2O&gt;</code> или включить <code>embeddedRouting</code>.
        </p>
      </div>
    );
  }

  return (
    <ConnectedRouter history={history}>
      <Switch>
        {React.Children.map(children, child =>
          React.cloneElement(child, {
            key: child.props.name,
            custom: true,
          })
        )}
        {embeddedRouting ? (
          <Route
            path="/:pageUrl*"
            render={routeProps => {
              debugger;
              return <Page {...routeProps} needMetadata rootPage />;
            }}
          />
        ) : null}
      </Switch>
    </ConnectedRouter>
  );
}

Router.propTypes = {
  children: PropTypes.oneOfType([
    PropTypes.arrayOf(PropTypes.node),
    PropTypes.node,
  ]),
  embeddedRouting: PropTypes.bool,
};

Router.defaultProps = {
  embeddedRouting: true,
};

export default pure(Router);
