## Evaluation of <em>AWS Lambda</em> research topic

### Evaluation criteria

For this research topic, we will make an evaluation based on three different aspects:

- **Quality of the tutorial**
- **Complexity of the topic**
- **Amount of research needed**

**Quality of the tutorial** is the most important part of the research topic because it is essential that the person who evaluates the topic can follow it without any problems or confusions.

**Complexity of the topic** is probably not that important for the evaluation, but we think that as we are Master students, it is also important to not do a trivial research.

And finally, the **amount of research needed** to develop the tutorial. It's not the same to do a tutorial based on some topic that was already explained in the course, that to do it about some other topic totally new for us, and that it takes more time to read documentation and learn about it.

And in the end, we will make an overall grade taking into account these three aspects of the research topic.

### Quality of the tutorial

Overall, the quality of the written part is very clear and has a good presentation with images and indicators on them, lists, recipes to follow and highlighted keywords.

The tasks are explained with a lot of detail, going step by step. That is really good if the tutorial is addressed to a nonspecialist audience, which is not the case. So we think some redundant steps could be skipped.

While following the tutorial, we've faced many errors:

- The first Lambda function didn't add the S3 bucket, returned an error, so we had to added manually.
- Some commands not work on Windows, and the tutorial does not specify which O.S. is required.
- Tutorial does not warn us to change the google_cloud_key in some copy/paste code step.
- Some errors when adding a new role in the Lambda function creation, we couldn't create the function, so we had to research a bit. It seems there is a race between IAM creating the rol and Lambda using this same role on the Lambda function. The solution was to wait a few minutes.
- Task 5 gave us an internal server error.

Also, a minor issue is that we saw some wrong links, like the "sample@@@@@file" one.

### Complexity of the topic

We have found that this research topic has a medium level complexity. Not too high nor too low. So we do not have any problem with respect to this aspect.

### Amount of research needed

About the amount of research needed to develop this topic, we think is not too much. The topic takes some things we've already seen in the lab sessions and goes a little deeper, in particular with the use of AWS Lambda. 

The tasks to do with Google Cloud, DynamoDB, S3 and IAM are similar to that the ones we did in the lab sessions.

So yes, we think that this research topic only goes a little further with AWS Lambda utilities.

### Conclusions

Making an evaluation on the three aspects, we come to the conclusion to grade this research topic with a **7**.
