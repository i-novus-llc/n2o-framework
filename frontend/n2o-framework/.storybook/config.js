import React from 'react';
import { configure, setAddon, addDecorator } from '@storybook/react';
import { setOptions } from '@storybook/addon-options';
import JSXAddon from 'storybook-addon-jsx';

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

setAddon(JSXAddon);

addDecorator( (story, path) =>
{
  // if (process.env.NODE_ENV !== 'production') {
  //   const {whyDidYouUpdate} = require('why-did-you-update');
  //   whyDidYouUpdate(React);
  // }
  return <Container story={story} path={path} />
});

function loadStories() {
  const req = require.context('../src', true, /\.story\.js$/);
  req.keys().forEach(filename => req(filename));
}

configure(loadStories, module);