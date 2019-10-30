import React from 'react';
import { configure, addDecorator } from '@storybook/react';
import { setOptions } from '@storybook/addon-options';
import { withInfo } from '@storybook/addon-info';

import Container from './Container';

import '../src/sass/n2o.scss';

// Option defaults:
setOptions({
  name: 'N2O Storybook',
  goFullScreen: false,
  showStoriesPanel: true,
  showAddonPanel: true,
  addonPanelInRight: true,
  sortStoriesByKind: true,
});

addDecorator(
  withInfo({
    source: false,
    inline: true,
    styles: {
      infoBody: {
        padding: 0,
      },
      infoStory: {
        margin: '20px 0',
      },
    },
  })
);

addDecorator((story, path) => {
  // if (process.env.NODE_ENV !== 'production') {
  //   const {whyDidYouUpdate} = require('why-did-you-update');
  //   whyDidYouUpdate(React);
  // }
  return <Container story={story} path={path} />;
});

function loadStories() {
  const req = require.context('../src', true, /\.story\.js$/);
  req.keys().forEach(filename => req(filename));
}

configure(loadStories, module);
