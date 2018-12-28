import React from 'react';
import { push } from 'connected-react-router';

import compileUrl from '../../../utils/compileUrl';

export default function linkImpl({ dispatch, state, target, path, pathMapping, queryMapping }) {
  const newUrl = compileUrl(path, { pathMapping, queryMapping }, state);
  if (target === 'application') {
    dispatch(push(newUrl));
  } else if (target === 'self') {
    window.top.location = newUrl;
  } else {
    window.top.open(newUrl);
  }
}
