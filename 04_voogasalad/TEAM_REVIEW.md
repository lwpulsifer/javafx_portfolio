CompSci 308: Team Review
===================

## Team
 
### Team Work Environment

I was very proud of our team work environment. I think we did very effective work together, especially when 
we were all in the same room. 

I think a large part of what made us work well together was that we really tried to become a team, in that we
all had each other's back and became good friends. Our communication was really good -- we made a big slack
channel, and added several smaller channels for each individual team. That helped us stay connected and motivated,
and it was easy to organize team meetings. Our easy, friendly team dynamic also made it easy to be flexible about
which team members could contribute the most at which times. If a teammate had an exam or a large assignment,
it was easy for them to ask for help from the rest of the team, and the team was always accomodating.

I think we could have improved on our collaboration between sub-teams. I think we were great collaborators overall, 
but there was some repeated code among subteams (i.e. between Player and Authoring or Data and Engine) that could 
have been resolved by better communication and API design. I think that we also could have improved upon our allocation
of work because sometimes people would finish their own work and then just relax instead of helping out with others.

#### What were your team's goals for the project and do you think they were clear to everyone?

I think this question can only be answered well in terms of sprints. For the sprint leading up to the first demo,
our team had very well-defined and clear goals. We wanted to make a simple end-to-end demo allowing a user to 
create a few bricks and a mario and to define their traits, then save the game and load it to the player for play.
These simple goals made it really easy for everyone to be involved because it was so obvious what each group needed
to do in order to accomodate everyone else's work. 
After that demo, however, we didn't quite do as much planning and goal-setting as we should have for the complete 
implementation, so I think that our goals weren't very clear to everyone. Authoring, for example, wanted to be able
to add components to levels, to create robust events, and to be able to load games for editing. Data, however, was 
working on Firebase integration and so wasn't focused on accomodating those changes. There were several instances
of this kind of miscommunication, which I think was simply a result of poor goal setting.

#### How was work divided among the team members and do you think everyone was clear about their responsibilities?

The sub-teams were as follows:

* Data: Brandon Guo, Harry Wang
* Player: Brandon Dalla Rosa, Dana Park
* Engine: Ben Hubsch, Jeremy Chen, Kevin Deng, Andy Nguyen
* Authoring: Liam Pulsifer, Jennifer Chin, Elizabeth Schulman

I think that everyone was very clear as to their responsiblities, sometimes to a fault. What I mean by that is that
after the midpoint demo, there was a distinct change in the work-flow that the project demanded. Author needed to wait
for Engine in order to take advantage of the new Events paradigm, while Player really needed to wait for Authoring
in order to design new features. Then, when Engine was done with their Events, they had comparatively little work, 
while Authoring and Player had a lot. What I think should have happened is that we should have been a little more
flexible after the midpoint demo in allocating work as it needed to be done rather than strictly by sub-team. I think
our divisions were sensible overall, but they led to each team having periods of very little work and periods of intense
work, which I don't think was a super healthy way of doing the project work. 

#### How was the communication within the team (was it too much, too little, just right) and why?

Our communication was, overall, very good. As I mentioned earlier, we had a Slack channel which made communication
easy and fun. I think there's always room for more communication in terms of making sure everyone knows exactly
where they are and what they should be doing, but there's also a limit to how much you can expect reasonable people
to talk to each other. One particular way that we could have improved, again as I mentioned above, was in communicating
our exact coding decisions to one another. This would have helped our work-flow by avoiding repeated code across 
sub-teams. I also felt that while our communication was good, our team members sometimes just did their 
own thing, regardless of what they had said they would do in our written communication. For example, on the day 
of the code freeze, our data team changed several method names and parameter sets without telling anyone, which really
messed with our build. 

#### How clear was the work flow of committing code and how easy did it make sharing progress and why?

This, I think, was the worst part of our team dynamic. Our Git practices were not as standardized as they should have 
been. Some team members, especially in the Engine, were in the practice of making many, many new branches whenever
they encountered an issue, without defining what those branches were for or exactly why they were made. Some people
committed and merged their branches to a central branch regularly, while some people only merged when they were 
absolutely sure of their build. This led to a lot of confusion, and to a lot of questions in the slack like 
"Which branch has the most recent engine stuff?". In the future, I would definitely standardize team branches so that
everyone could know at all times where the most recent working builds were.

### Rate Your Teammates

Rate your fellow students as team mates on the following scale:

 * -5 (no contribution)
 *  0 (not enough information to rate)
 * +5 (amazing team leader/designer)


| Student Name | NetID | Rating |
| ------------ | ----- | ------ |
| Kevin Deng	| ktd9 | 2 |
| Andy Nguyen	| aln20 | 2 |
| Jennifer Chin | jrc81 | 4 |
| Brandon Guo	| blg19 | -1 |
| Elizabeth Shulman | ers58 | 4 |
| Harry Wang | hyw2 | 2 |
| Dana Park | dp178 | 2 |
| Brandon Dalla Rosa | bkd8 | 1 |
| Jeremy Chen | jc587 | 4 |
| Ben Hubsch | bah37 | 5 |

#### For any team mate you rated a 3 or higher or lower than 0, provide specific details for your evaluation

###### For the record, a rating of 2 on my scale symbolizes "Good teammate, but I didn't interact with them enough
###### from a coding perspective to be able to talk specifically about their design or team-coding practices"

Jennifer Chin: Acted as a really great leadership presence and get-it-done kind of person for our team. She was
always the first person to speak up at team meetings when people started to get off track, and she really excelled
at keeping team conversation flowing and helping to amplify good ideas, especially when they came from quieter members
of the team. Her code, as well, was always on point, and her design ideas were great.

Brandon Guo: Brandon was a great guy to have around in the team, but his work was sometimes lackadaisical, 
especially when it came to justifying his design decisions. For example, his data framework included two maps,
a metadata map and a settings map. However, both simply mapped strings to strings, and he was never able to 
articulate to the rest of the team why the two should be separate, instead just plugging away with that design,
despite our repeated attempts to help him refactor.

Elizabeth Schulman: Elizabeth was a fantastic presence on our team. She could always be relied upon to make sure
that our project had everything it needed to succeed, whether that meant creating Google docs, making sure we were 
up on the assignment specs, making our final demo video, or simply sitting down to bang out some code. She was also
an impeccable UX designer who was responsible for creating the look of our project almost entirely.

Jeremy Chen: Jeremy was a really hard worker and fantastic designer, who, along with Ben, was responsible for almost
all of the ECS design that we used to create our project. He was the sole architect behind Collisions, and while some 
of his ideas didn't end up working, he was always a font of ideas for how to get things done. He was also always ready
to help out with areas of the project other than engine -- he wrote up resource files for Authoring, and helped to 
create factories which made our life much easier.

Ben Hubsch: Ben was our rock star. He was an ace designer, implementer, debugger, what-have-you. When we completely
broke our project on the night of the code freeze, he was the one who sat down and debugged it for hours until it 
worked again. He designed games, he wrote huge sections of the engine, he wrote factories to create engine components,
he wrote resource files. The man did it all for our team.

## Personal

### My Rating

#### 3

I wrote a large majority of the code governing interaction between authoring and engine, and my design made it really
easy for the two to interact. I was also the principle person that people came to for help with Git, submodules, and
general debugging, and was the point person for authoring when anyone ran into authoring problems. 

### My Role on the Team

My primary role on the team was as implementer, especially in the code that created Engine elements in authoring. I 
wrote the classes which allowed Authoring to interact with Engine and actually create entities and components. I also
spent large amounts of time designing, designing an easy GUI hierarchy and a system of menuElements which made it
easy to read in data from Engine and display components to the user for editing.

## Summary

### My biggest strength as a team member

My biggest strength as a team member is my ability to incorporate different ideas into a cohesive whole. When Authoring
was deciding how to do our Entity creation, for example, I was able to combine a couple of different ideas from our team
into a unified system with multiple separate controllers. This combined the strengths of an observer-observable pattern that
one teammate had proposed with the best aspects of a Model-View-Controller model. 

### My biggest weakness as a team member

My biggest weakness is my reluctance to ask for help. As the project deadline approached, I found myself with a 
lot more work than I had anticipated -- honestly, an unreasonable amount of work for any one person to do. However,
I struggled through it and ended up with an inferior authoring product as a result. I should have asked some of 
my other teammates who had less work to help me finish up authoring so that we could have had a really polished 
final product.

### How I improved as a team mate this semester

I definitely have improved with my communication and my leadership this semester. In two of my projects this year,
I ended up being thrust into a leadership role because my teammates were younger or less experienced, and it was 
a huge lesson in how to manage a team. I've especially learned how to balance the different strengths of my 
teammates and make sure that people aren't being forced into doing things that they don't want to do. In my Slogo team,
I had a teammate who had done a bunch of front end work in the last project and was very skilled at it, but who didn't 
really enjoy that side of design, so we worked together to find a way to use his front end skills while also letting
him work closely with the back end to instantiate commands.