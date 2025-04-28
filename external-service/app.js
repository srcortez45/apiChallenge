const express = require('express');
const app = express();
const port = 3000;

app.get('/api/random-number', (req, res) => {
  const randomNumber = Math.floor(Math.random() * 100) + 1;
  console.log(`Generated random number: ${randomNumber}`);
  res.json(randomNumber);
});

app.listen(port, () => {
  console.log(`Server running on http://localhost:${port}`);
});
