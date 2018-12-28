import React from 'react';
import { Route, Link } from 'react-router-dom';

const ChildId = ({ match }) => (
  <div>
    <h3>ID: {match.params.id}</h3>
  </div>
);

const ComponentParams = () => (
  <div>
    <ul>
      <li>
        <Link to="/accounts/netflix">Netflix</Link>
      </li>
      <li>
        <Link to="/accounts/sky">Sky</Link>
      </li>
    </ul>
    <Route path="/accounts/:id" component={ChildId} />
  </div>
);

export default ComponentParams;
